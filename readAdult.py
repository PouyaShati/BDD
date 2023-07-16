

name_list = []

for i in range(4, 17):
  name_list.append("d" + str(i))

name_list += ["d4_m2", "d6_m2", "d8_m2", "d10_m2", "d12_m2", "d14_m2", "d16_m2"]
name_list += ["d6_m3", "d9_m3", "d12_m3", "d15_m3"]
name_list += ["d8_m4", "d12_m4", "d16_m4"]


point = 0

for nl in name_list:
  file = open("results/adult_b_t60_" + nl + "_pbdd", 'r')
  lines = file.readlines()
  found = False
  for line in lines:
    if line.startswith("Classify_cost:"):
      print(line.split()[1])
      found = True
  if not found:
    print("UNK")