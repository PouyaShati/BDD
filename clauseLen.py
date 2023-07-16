file = open("clauses/clauses_adult_b_d12", 'r')
lines = file.readlines()[1:]

sum = 0

for line in lines:
  sum += len(line.split())-2

print(sum/len(lines))
