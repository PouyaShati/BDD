
datasets = ['banknote', 'breast', 'cryotherapy', 'immunotherapy', 'ionosphere', 'iris', 'user', 'vertebral', 'wine', 'car', 'monk2']

print('var','clause','c_len')

for d in datasets:
  for h in range(4,7):
    var_sum = 0
    clause_sum = 0
    c_len_sum = 0   
    for f in range(5):
      file = open("results/" + d + "_d" + str(h) + "_f" + str(f) + "_pbdd", 'r')
      lines = file.readlines()
      for line in lines:
        if line.startswith('Number_of_variables:'):
          var_sum += float(line.split()[1])
        if line.startswith('Number_of_hard_clauses:'):
          clause_sum += float(line.split()[1])
        if line.startswith('Number_of_soft_clauses:'):
          clause_sum += float(line.split()[1])
        if line.startswith('Clause_average_length:'):
          c_len_sum += float(line.split()[1]) 
        if line.startswith('Status:'):
          break           
    print(var_sum/5, clause_sum/5, c_len_sum/5)


print('var','clause','c_len')

for d in datasets:
  for m in ['2','3','i','h']:
    var_sum = 0
    clause_sum = 0
    c_len_sum = 0   
    for f in range(5):
      file = open("results/" + d + "_d6_m" + m + "_f" + str(f), 'r')
      lines = file.readlines()
      for line in lines:
        if line.startswith('Number_of_variables:'):
          var_sum += float(line.split()[1])
        if line.startswith('Number_of_hard_clauses:'):
          clause_sum += float(line.split()[1])
        if line.startswith('Number_of_soft_clauses:'):
          clause_sum += float(line.split()[1])
        if line.startswith('Clause_average_length:'):
          c_len_sum += float(line.split()[1])    
        if line.startswith('Status:'):
          break        
    print(var_sum/5, clause_sum/5, c_len_sum/5)
