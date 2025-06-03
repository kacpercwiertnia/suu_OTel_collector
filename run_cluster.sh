#!/bin/bash

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

echo "####################################################"
echo "# Sprawdzanie Wymaganych Narzędzi                  #"
echo "####################################################"
echo ""

all_requirements_met=true
missing_requirements_details=()

check_command() {
  local cmd_name="$1"
  local version_command="$2"
  local install_url="$3"

  printf "Sprawdzanie %-10s ... " "$cmd_name"

  if command -v "$cmd_name" &> /dev/null; then
    printf "${GREEN}OK${NC}\n"
    if [[ -n "$version_command" ]]; then
      printf "  %-18s " "Wersja:"
      local version_output
      version_output=$(bash -c "$version_command" 2>/dev/null)

      if [[ -n "$version_output" ]]; then
        echo "$version_output" | head -n 1
      else
        echo "Nie udało się automatycznie ustalić wersji."
      fi
    fi
  else
    printf "${RED}BRAK${NC}\n"
    missing_requirements_details+=("Narzędzie '$cmd_name' nie zostało znalezione. Instrukcja instalacji: $install_url")
    all_requirements_met=false
  fi
  echo 
}

check_command "docker" "docker --version" "https://docs.docker.com/engine/install/"
check_command "kubectl" "kubectl version --client --short" "https://kubernetes.io/docs/tasks/tools/install-kubectl/"
check_command "kind" "kind --version" "https://kind.sigs.k8s.io/docs/user/quick-start/#installation"
check_command "helm" "helm version --short" "https://helm.sh/docs/intro/install/"

echo "----------------------------------------------------"
if $all_requirements_met; then
  echo -e "${GREEN}Gratulacje! Wszystkie wymagane narzędzia są zainstalowane i dostępne w PATH.${NC}"
else
  echo -e "${RED}Wykryto brakujące narzędzia:${NC}"
  for detail in "${missing_requirements_details[@]}"; do
    echo -e "  - $detail"
  done
  echo -e "\nProszę zainstalować brakujące narzędzia, korzystając z podanych linków, a następnie uruchomić skrypt ponownie."
  exit 1
fi
set -e

# --- Konfiguracja Ścieżek i Nazw ---
YAML_DIR="kubernetes"
KIND_CLUSTER_NAME="otel-cluster" # Zgodnie z Twoim cluster.yaml
KIND_CONTEXT_NAME="kind-${KIND_CLUSTER_NAME}" # Domyślny format kontekstu Kind

CERT_MANAGER_NAMESPACE="cert-manager"
CERT_MANAGER_VERSION="v1.14.5" # Wersja z Twoich kroków

INGRESS_NAMESPACE="ingress-nginx"
MONITORING_NAMESPACE="monitoring"
OTEL_COLLECTOR_NAMESPACE="default"
OTEL_OPERATOR_NAMESPACE="opentelemetry-operator-system" # Domyślny namespace dla operatora OTel, jeśli go instalujemy

# Kolory dla logów
YELLOW='\033[0;33m'
GREEN='\033[0;32m'
NC='\033[0m' # Brak koloru

echo -e "${YELLOW}Rozpoczynanie konfiguracji klastra Kubernetes z monitoringiem i OTel Collector...${NC}"

# --- Krok 1: Start klastra Kind ---
echo -e "\n${YELLOW}>>> Krok 1: Uruchamianie klastra Kind '${KIND_CLUSTER_NAME}'${NC}"
if kind get clusters | grep -q "^${KIND_CLUSTER_NAME}$"; then
  echo -e "    ${GREEN}INFO:${NC} Klaster Kind '${KIND_CLUSTER_NAME}' już istnieje. Pomijam tworzenie."
else
  kind create cluster --config "${YAML_DIR}/cluster.yaml"
  echo -e "    ${GREEN}INFO:${NC} Klaster Kind '${KIND_CLUSTER_NAME}' utworzony."
fi

# --- Krok 2: Ustawienie kontekstu kubectl ---
echo -e "\n${YELLOW}>>> Krok 2: Ustawianie kontekstu kubectl na '${KIND_CONTEXT_NAME}'${NC}"
kubectl cluster-info --context "${KIND_CONTEXT_NAME}" # Używam poprawnego kontekstu

# --- Krok 3: Dodawanie repozytoriów Helm ---
echo -e "\n${YELLOW}>>> Krok 3: Dodawanie repozytoriów Helm${NC}"
helm repo add jetstack https://charts.jetstack.io
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo add open-telemetry https://open-telemetry.github.io/opentelemetry-helm-charts
helm repo update
echo -e "    ${GREEN}INFO:${NC} Repozytoria Helm dodane i zaktualizowane."

# --- Krok 4: Instalacja Cert-Managera ---
echo -e "\n${YELLOW}>>> Krok 4: Instalacja Cert-Managera${NC}"
echo -e "    ${GREEN}INFO:${NC} Aplikowanie CRD Cert-Managera..."
kubectl apply -f "https://github.com/cert-manager/cert-manager/releases/download/${CERT_MANAGER_VERSION}/cert-manager.crds.yaml"
echo -e "    ${GREEN}INFO:${NC} Instalowanie Cert-Managera przez Helm..."
helm upgrade --install cert-manager jetstack/cert-manager \
  --namespace "${CERT_MANAGER_NAMESPACE}" \
  --create-namespace \
  --version "${CERT_MANAGER_VERSION}" \
  --wait
echo -e "    ${GREEN}INFO:${NC} Cert-Manager zainstalowany. Sprawdzanie statusu podów..."
kubectl get pods -n "${CERT_MANAGER_NAMESPACE}"
echo -e "    ${GREEN}INFO:${NC} Poczekaj chwilę i upewnij się manualnie, że pody są 'Running' i 'Ready'."
echo -e "    ${GREEN}INFO:${NC} Możesz użyć: kubectl wait --namespace \"${CERT_MANAGER_NAMESPACE}\" --for=condition=Ready pods --all --timeout=300s"


# --- Krok 5: Instalacja i Konfiguracja Kontrolera Ingress NGINX ---
echo -e "\n${YELLOW}>>> Krok 5: Instalacja Kontrolera Ingress NGINX${NC}"
helm upgrade --install ingress-nginx ingress-nginx/ingress-nginx \
  --namespace "${INGRESS_NAMESPACE}" \
  --create-namespace \
  --values "${YAML_DIR}/ingress-values.yaml" \
  --wait
echo -e "    ${GREEN}INFO:${NC} Kontroler Ingress NGINX zainstalowany. Sprawdzanie statusu podów..."
kubectl get pods -n "${INGRESS_NAMESPACE}"
echo -e "    ${GREEN}INFO:${NC} Poczekaj chwilę i upewnij się manualnie, że pody są 'Running' i 'Ready'."
echo -e "    ${GREEN}INFO:${NC} Możesz użyć: kubectl wait --namespace \"${INGRESS_NAMESPACE}\" --for=condition=Ready pods --selector=app.kubernetes.io/component=controller --timeout=120s"

# --- Krok 6: Instalacja Prometheus & Grafana (kube-prometheus-stack) ---
echo -e "\n${YELLOW}>>> Krok 6: Instalacja Prometheus & Grafana (kube-prometheus-stack)${NC}"
helm upgrade --install monitoring prometheus-community/kube-prometheus-stack \
  --namespace "${MONITORING_NAMESPACE}" \
  --create-namespace \
  --values "${YAML_DIR}/monitoring-values.yaml" \
  --wait
echo -e "    ${GREEN}INFO:${NC} kube-prometheus-stack zainstalowany. Sprawdzanie statusu podów..."
kubectl get pods -n "${MONITORING_NAMESPACE}"
echo -e "    ${GREEN}INFO:${NC} Poczekaj chwilę i upewnij się manualnie, że pody są 'Running' i 'Ready'."
echo -e "    ${GREEN}INFO:${NC} Możesz użyć: kubectl wait --namespace \"${MONITORING_NAMESPACE}\" --for=condition=Ready pods --all --timeout=600s"

# --- Krok 7: Tworzenie instancji OpenTelemetry Collector ---

helm install opentelemetry-operator open-telemetry/opentelemetry-operator \\
  --namespace opentelemetry-operator-system \\
  --create-namespace 
  
echo -e "\n${YELLOW}>>> Krok 7: Tworzenie instancji OpenTelemetry Collector${NC}"
kubectl apply -f "${YAML_DIR}/otel-config.yaml"
echo -e "    ${GREEN}INFO:${NC} Konfiguracja OpenTelemetry Collector zastosowana. Sprawdzanie statusu podów..."
# Poczekaj chwilę na utworzenie deploymentu
sleep 10
echo -e "    ${GREEN}INFO:${NC} Możesz użyć: kubectl wait --namespace \"${OTEL_COLLECTOR_NAMESPACE}\" --for=condition=Available deployment -l app.kubernetes.io/instance=${OTEL_COLLECTOR_NAMESPACE}.my-otel-collector --timeout=300s"
kubectl get pods -n "${OTEL_COLLECTOR_NAMESPACE}" -l app.kubernetes.io/instance="${OTEL_COLLECTOR_NAMESPACE}.my-otel-collector"


# --- Krok 8: Tworzenie ServiceMonitor dla OpenTelemetry Collector ---
echo -e "\n${YELLOW}>>> Krok 8: Tworzenie ServiceMonitor dla OpenTelemetry Collector${NC}"
kubectl apply -f "${YAML_DIR}/otel-collector-service-monitor.yaml" -n "${MONITORING_NAMESPACE}"
echo -e "    ${GREEN}INFO:${NC} ServiceMonitor dla OpenTelemetry Collector zastosowany."

echo -e "\n${GREEN}####################################################"
echo -e "#          SKRYPT ZAKOŃCZYŁ DZIAŁANIE!           #"
echo -e "####################################################${NC}"
echo -e "Sprawdź status wszystkich wdrożonych komponentów."
echo -e "Grafana powinna być dostępna pod: http://localhost:3000"
echo -e "OTLP/gRPC (przez Ingress) na: localhost:4317"
echo -e "OTLP/HTTP (przez Ingress) na: localhost:4318"

# === KONIEC SKRYPTU ===