
categories = []

categories.append([])
categories.append(["Private", "Self-emp-not-inc", "Self-emp-inc", "Federal-gov", "Local-gov", "State-gov", "Without-pay", "Never-worked"])
categories.append([])
categories.append(["Bachelors", "Some-college", "11th", "HS-grad", "Prof-school", "Assoc-acdm", "Assoc-voc", "9th", "7th-8th", "12th", "Masters", "1st-4th", "10th", "Doctorate", "5th-6th", "Preschool"])
categories.append([])
categories.append(["Married-civ-spouse", "Divorced", "Never-married", "Separated", "Widowed", "Married-spouse-absent", "Married-AF-spouse"])
categories.append(["Tech-support", "Craft-repair", "Other-service", "Sales", "Exec-managerial", "Prof-specialty", "Handlers-cleaners", "Machine-op-inspct", "Adm-clerical", "Farming-fishing", "Transport-moving", "Priv-house-serv", "Protective-serv", "Armed-Forces"])
categories.append(["Wife", "Own-child", "Husband", "Not-in-family", "Other-relative", "Unmarried"])
categories.append(["White", "Asian-Pac-Islander", "Amer-Indian-Eskimo", "Other", "Black"])
categories.append(["Female", "Male"])
categories.append([])
categories.append([])
categories.append([])
categories.append(["United-States", "Cambodia", "England", "Puerto-Rico", "Canada", "Germany", "Outlying-US(Guam-USVI-etc)", "India", "Japan", "Greece", "South", "China", "Cuba", "Iran", "Honduras", "Philippines", "Italy", "Poland", "Jamaica", "Vietnam", "Mexico", "Portugal", "Ireland", "France", "Dominican-Republic", "Laos", "Ecuador", "Taiwan", "Haiti", "Columbia", "Hungary", "Guatemala", "Nicaragua", "Scotland", "Thailand", "Yugoslavia", "El-Salvador", "Trinadad&Tobago", "Peru", "Hong", "Holand-Netherlands"])

outputs = [">50K", "<=50K"]

values_map = {}

for cat in categories:
  for c in range(len(cat)):
    str = ""
    for i in range(len(cat)):
      if i == c:
        str = str + "1 "
      else:
        str = str + "0 "
    values_map[cat[c]] = str


values_map["<=50K"] = "1"
values_map[">50K"] = "2"

print(values_map)


file = open('adult.data','r')
lines = file.readlines()
        
for line in lines:
  parts = line.split(',')
  for p in range(len(parts)):
    if parts[p] == "?":
      if len(categories[p]) == 0:
        print("Error!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
      for i in range(len(categories[p])):
        print("0 ",end='')
    elif parts[p] in values_map:
      print(values_map[parts[p]], end='')
    elif parts[p].startswith('>50K'):
      print('2')
    elif parts[p].startswith('<=50K'):
      print('1')
    else:
      print(parts[p], end=' ')