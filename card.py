from pysat.card import *
import sys

start = int(sys.argv[1])
end = int(sys.argv[2])
count = int(sys.argv[3])
index = int(sys.argv[4])


cnf = CardEnc.atmost(lits=[i for i in range(start,end+1)], encoding=EncType.seqcounter, bound=count, top_id=index)
maxVar = 0
averageLen = 0.0
for c in cnf.clauses:
  maxVar = max(maxVar, max(c), -min(c))
  averageLen += len(c)
  for l in c:
    print(l, end=' ')
  print()
averageLen = averageLen / len(cnf.clauses)

print("===")
print(maxVar)

#print("***************************")
#print(start)
#print(end)
#print(count)
#print(index)
#print("new var:", (maxVar-index))
#print("Average len", averageLen)
#print("Clause cnt:",len(cnf.clauses))


