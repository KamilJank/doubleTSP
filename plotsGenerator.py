import matplotlib.pyplot as plt
import networkx as nx
import numpy as np

def readPositions(fileName):
    f = open(fileName, "r")
    lines=f.readlines()
    positions=[l.split(' ') for l in lines[6:-1]]
    positions=[[int(p[1]),int(p[2][:-1])] for p in positions]
    return positions

def getPairs(tsp):
    pairs=[]
    for i in range(len(tsp)-1):
        pairs.append((tsp[i],tsp[i+1]))
    pairs.append((tsp[len(tsp)-1],tsp[0]))
    return pairs

def readVertices(fileName):
    f = open(fileName, "r")
    solution=[int(n) for n in f.readlines()[1].split(';')[0][1:-1].split(', ')]
    size=len(solution)
    halfsize=int(size/2)
    tsp=[solution[:halfsize],solution[halfsize:]]
    return [getPairs(ts) for ts in tsp]

def drawPaths(positions,pairs):
    edges=[*pairs[0],*pairs[1]]
    G=nx.DiGraph()
    G.add_nodes_from(range(len(positions)))
    G.add_edges_from(edges)
    nx.draw(G,pos=positions,node_size=10,arrows=False)
    plt.show()
    #TODO save not show

def drawAllGraphs():
    for instance in ["kroA100","kroA150","kroB100","kroB150"]:
        pos=readPositions("./data/"+instance+".tsp")
        for experiment in ["1/heuristic1_","1/heuristic2_","2/heuristic1_","2/heuristic2_","2/random_","3/random_","5_1/h1_"]:
            ver=readVertices("./results/"+experiment+instance+".txt")
            drawPaths(pos,ver)

def drawSimilarityPlot(fileName):
    f = open(fileName, "r")
    lines=np.array([l.split(';') for l in f.readlines()[1:]])
    similarity=np.array([[float(l[0]),int(l[1][:-3])] for l in lines])
    plt.plot(similarity[:,0], similarity[:,1], 'k.')
    plt.show()
    #TODO save not show

def drawAllSimPlots():
    for experiment in ["4/random_","5_2/h1_"]:
        for instance in ["kroA100","kroA150","kroB100","kroB150"]:
            drawSimilarityPlot("./results/"+experiment+instance+".txt")
#drawAllGraphs()
drawAllSimPlots()