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
  exit 0
else
  echo -e "${RED}Wykryto brakujące narzędzia:${NC}"
  for detail in "${missing_requirements_details[@]}"; do
    echo -e "  - $detail"
  done
  echo -e "\nProszę zainstalować brakujące narzędzia, korzystając z podanych linków, a następnie uruchomić skrypt ponownie."
  exit 1
fi