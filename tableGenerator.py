import numpy as np
import csv
def generateTable(experimentName):
    table=[]#["Jakość najlepszego rozwiązania","Jakość najgorszego rozwiązania","Średnia jakość rozwiązań","Średni czas działania"]
    for instance in ["kroA100","kroA150","kroB100","kroB150"]:
        f = open("./results/"+experimentName+instance+".txt", "r")
        lines=f.readlines()
        f.close()
        bestSol=float(lines[1].split(';')[1])
        worstSol=float(lines[2].split(';')[1])
        avgSol=float(lines[3].split(';')[2])
        time=float(lines[3].split(';')[1])/1000000000
        table.append([bestSol,worstSol,avgSol,time])
    a = np.array(table)
    np.savetxt("./results/"+experimentName+".csv",a,fmt='%1.4f', delimiter=';',header="Jakość najlepszego rozwiązania;Jakość najgorszego rozwiązania;Średnia jakość rozwiązań;Średni czas działania")



def generateAllTables():
    for experiment in ["1/heuristic1_","1/heuristic2_","2/heuristic1_","2/heuristic2_","2/random_","3/random_","5_1/h1_"]:
        generateTable(experiment)

def generatePermutationsTable(experimentName):
    table=[]#["Jakość najlepszego rozwiązania","Jakość najgorszego rozwiązania","Średnia jakość rozwiązań","Średni czas działania"]
    writer = open("./results/"+experimentName+"perms.txt", "w")
    writer.write("Instancja & Lista wierzchołków 1 & Lista wierzchołków 2 & Jakość rozwiązania\n")
    for instance in ["kroA100","kroA150","kroB100","kroB150"]:
        f = open("./results/"+experimentName+instance+".txt", "r")
        line=f.readlines()[1].split(';')
        f.close()
        perm=line[0][1:-1].split(", ")
        size=int(len(perm)/2)
        p1=', '.join(str(e) for e in perm[:size])
        p2=', '.join(str(e) for e in perm[size:])
        writer.write(instance+" & "+p1+" & "+p2+" & "+line[1])
    writer.close()

def generateAllTables2():
    for experiment in ["1/heuristic1_","1/heuristic2_","2/heuristic1_","2/heuristic2_","2/random_","3/random_","5_1/h1_"]:
        generatePermutationsTable(experiment)

generateAllTables2()