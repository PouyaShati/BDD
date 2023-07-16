# bead_cnt

file = open("pbdd_temp", 'r')
lines = file.readlines()

point = 0

for d in range(11):
  for dim in range(4):
      vals = [0 for i in range(5)]
      for f in range(5):
        vals[0] += float(lines[point].split()[0])
        vals[1] += float(lines[point].split()[1])
        vals[2] += float(lines[point].split()[2])
        vals[3] += float(lines[point].split()[3])
        vals[4] += float(lines[point].split()[4])
        point += 1
            
      for val in vals:
        print(val/5,end=' ')
      print()
