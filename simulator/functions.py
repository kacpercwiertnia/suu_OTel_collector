import numpy as np

def cardioid(t):
    a = 0.25
    return 2 * a * (1 - np.cos(t)) * np.cos(t), 2 * a * (1 - np.cos(t)) * np.sin(t) 

def bernoulie(t):
    a = 0.25
    return (a * 1.414 * np.cos(t)) / (1 + np.sin(t) ** 2), (a * 1.414 * np.cos(t) * np.sin(t)) / (1 + np.sin(t) ** 2)

def asteroid(t):
    a = 0.25
    return (a * np.cos(t) ** 3), (a * np.sin(t) ** 3)

def circle(t):
    a = 0.25
    return (a * np.cos(t)), (a * np.sin(t))

def three_leaved_clover(t):
    return (np.cos(3 * t) * np.cos(t)), (np.cos(3 * t) * np.sin(t))

def deltoid(t):
    a = 0.25
    return (2 * a * np.cos(t) + a * np.cos(2*t)), (2 * a * np.sin(t) - a * np.sin(2*t))

def get_all_functions():
    return [cardioid, bernoulie, asteroid, circle, three_leaved_clover, deltoid]