# bead_cnt

file = open("cw_temp_bead", 'r')
lines = file.readlines()

file2 = open("pbdd_temp", 'r')
lines2 = file2.readlines()
point2 = 0

point = 0

for d in range(11):
  for h in range(3):
      cws = []
      for w in range(4):
        new_cw = 0
        for f in range(5):
          new_cw += int(lines[point])
          point += 1
        cws.append(round(new_cw/5,1))
      new_cw = 0
      for f in range(5):
        new_cw += int(lines2[point2].split()[0])
        point2 += 1
      cws.append(round(new_cw/5,1))  

      for cw in range(len(cws)-1):
        print(cws[cw],end=',')
      print(cws[-1])


print("====================================")
point2 = 0

file = open("cw_temp_train_acc", 'r')
lines = file.readlines()

point = 0

for d in range(11):
  for h in range(3):
      cws = []
      for w in range(4):
        new_cw = 0
        for f in range(5):
          new_cw += float(lines[point])
          point += 1
        cws.append(round((1-new_cw/5)*100,1))

      new_cw = 0
      for f in range(5):
        new_cw += float(lines2[point2].split()[1])
        point2 += 1
      cws.append(round((1-new_cw/5)*100,1))  

      
      for cw in range(len(cws)-1):
        print(cws[cw],end=',')
      print(cws[-1])



print("====================================")
point2 = 0

file = open("cw_temp_test_acc", 'r')
lines = file.readlines()

point = 0

for d in range(11):
  for h in range(3):
      cws = []
      for w in range(4):
        new_cw = 0
        for f in range(5):
          new_cw += float(lines[point])
          point += 1
        cws.append(round((1-new_cw/5)*100,1))

      new_cw = 0
      for f in range(5):
        new_cw += float(lines2[point2].split()[2])
        point2 += 1
      cws.append(round((1-new_cw/5)*100,1)) 
      
      for cw in range(len(cws)-1):
        print(cws[cw],end=',')
      print(cws[-1])





print("====================================")
point2 = 0


file = open("cw_temp_time", 'r')
lines = file.readlines()

point = 0

for d in range(11):
  for h in range(3):
      cws = []
      for w in range(4):
        new_cw = 0
        for f in range(5):
          new_cw += float(lines[point])
          point += 1
        val = round((1.0 * new_cw/5/1000),2)
        if val < 10:
          cws.append("<10")
        else:
          cws.append(val)

      new_cw = 0
      for f in range(5):
        new_cw += float(lines2[point2].split()[3])
        point2 += 1
      val = round((1.0 * new_cw/5/1000),2)      
      if val < 10:
        cws.append("<10")
      else:
        cws.append(val)


      
      for cw in range(len(cws)-1):
        print(cws[cw],end=',')
      print(cws[-1])

