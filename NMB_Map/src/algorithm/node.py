class Node:
    def __init__(self, id, x, y, neighbors=[]):
        self.id = id
        self.map = map
        self.coordinates = (x, y)
        self.x = x
        self.y = y
        self.neighbors = neighbors

    def __str__(self):
        return str(self.id) + ": " + str(self.coordinates)
