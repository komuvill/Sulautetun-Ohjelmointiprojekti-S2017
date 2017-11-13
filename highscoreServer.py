import socket
import pymysql

class highscoreServer:
    def __init__(self, host, port):
        self.host = host
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.settimeout(None)
        self.db = pymysql.connect(host="mysli.oamk.fi", user="t6kovi01", passwd="smp7Nn7ZZduRg6M8", db="opisk_t6kovi01")
        self.cur = self.db.cursor()
    def initialise(self):
        try:
            self.sock.bind((self.host, self.port))
        except socket.error:
            print("Socket error in initialise function")
            return
        self.sock.listen(5)
        print("Server running on ",self.host,"and listening to ", self.port)

    def serve_forever(self):
        try:
            request, client_address = self.sock.accept()
            print("Received connection from ", client_address)
            data = request.recv(20)
            stringdata = data.decode('utf-8', 'ignore')
            stringdata = stringdata[2:len(stringdata)]
            print("Received data: ", stringdata)
            request.send("Received data, go fuck yourself!".encode('utf-8'))
        except socket.error:
            print("Socket error in serve_forever-function")
            return
        finally:
            request.close()

   #def insertHighscores(name, score):
        #Työn alla
        #self.cur.execute("INSERT INTO leaderboards(name, points) VALUES('",name,"','",score,"') ")
        
        
        

def main():
    host = "10.4.0.97"
    server = highscoreServer(host, 20000)
    server.initialise()
    while True:
        server.serve_forever()

if __name__ == "__main__":
    main()
