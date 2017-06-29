from random import randint
import string
b=""
for x in range(0,300000):
    a=randint(1,255)
    b += str(chr(a))

print(b)
    
#print (my_list)
#with open("test.txt", "a") as myfile:
#   myfile.write(b)

