import os.path




def is_beat(front, sol):
  for l in front:
    if l[0] <= sol[0] and l[1] >= sol[1]:
      return True
  return False

def remove_beat(front, sol):
  l = len(front)-1
  while l >= 0:
    if is_beat([sol], front[l]):
      del front[l]

    l -= 1



def get_average(filenames):

      size_sum = 0
      test_acc_sum = 0

      for f in range(5):  
        file = open('results/' + filenames[f], 'r')

        lines = file.readlines()
        new_size = 1000
        new_test_acc = 0

        for line in lines:
          if line.startswith("Bead_count:") or line.startswith("Post_bead_count:"):
            cand_size = float(line.split()[1])
            if cand_size < new_size:
              new_size = cand_size

          if line.startswith("Testing_acc:"):
            new_test_acc = (1-float(line.split()[1]))*100

        size_sum += new_size
        test_acc_sum += new_test_acc

      return [size_sum/5, test_acc_sum/5]








datasets = ['banknote', 'breast', 'cryotherapy', 'immunotherapy', 'ionosphere', 'iris', 'user', 'vertebral', 'wine', 'car', 'monk2']

dataset_colors = ['red','green','blue','yellow','magenta','cyan','brown','lime','olive','orange','pink']

heights = ['4', '5', '6']

weights = ['1', '3', '10', '_inf']

mults = ['2', '3', 'i', 'h']

for d_ind in range(len(datasets)):
  d = datasets[d_ind]
  front = []
  for h in heights:
    filenames = [d + '_d' + h + '_f0_pbdd', d + '_d' + h + '_f1_pbdd', d + '_d' + h + '_f2_pbdd', d + '_d' + h + '_f3_pbdd', d + '_d' + h + '_f4_pbdd']
    sol = get_average(filenames)

    if not is_beat(front, sol):
      remove_beat(front, sol)
      front.append(sol + ['2s' ,'d' + h])

  for m in mults:
    filenames = [d + '_d6_m' + m + '_f0', d + '_d6_m' + m + '_f1', d + '_d6_m' + m + '_f2', d + '_d6_m' + m + '_f3', d + '_d6_m' + m + '_f4']
    sol = get_average(filenames)
    if not is_beat(front, sol):
      remove_beat(front, sol)
      front.append(sol + ['m', 'm' + m])
  
  for h in heights:
    for w in weights:
      filenames = [d + '_d' + h + '_f0_bdd_cw' + w, d + '_d' + h + '_f1_bdd_cw' + w, d + '_d' + h + '_f2_bdd_cw' + w, d + '_d' + h + '_f3_bdd_cw' + w, d + '_d' + h + '_f4_bdd_cw' + w]
      sol = get_average(filenames)
      if not is_beat(front, sol):
        remove_beat(front, sol)
        front.append(sol + ['1s','w' + w, 'd' + h])
  to_print = sorted(front, key=lambda x: x[0])
  #print(d)


  #print('\\draw[color=' + dataset_colors[d_ind] + '] ',end='')

  for p in to_print:
    if p[0] > 10 or p[1] < 75:
      continue

    if p[-1].startswith('m'):
      col = 'green'
    elif p[-1].startswith('d') and not p[-2].startswith('w'):
      col = 'black'
    elif p[-2].startswith('w'):
      col = 'red'
      if p[-2] == 'w_inf':
        col = 'red!70!black'
      elif p[-2] == 'w10':
        col = 'red!80!black'
      elif p[-2] == 'w3':
        col = 'red!90!black'
      elif p[-2] == 'w1':
        col = 'red'
    
    if p[-1].startswith('m'):
      shp = '*'
    elif p[-1][1:] == '6':
      shp = '*'
    elif p[-1][1:] == '5':
      shp = 'square*'
    elif p[-1][1:] == '4':
      shp = 'triangle*'

    print('\\draw[color=' + col + '] plot[mark=' + shp + '] coordinates {(' + str(p[0]/10) + ' , ' + str(p[1]/100) + ')};')

  print('\\draw[color=grey,no markers]', end='')
  for p in to_print:
    if p[0] > 10 or p[1] < 75:
      continue

    print('plot coordinates {(' + str(p[0]/10) + ', ' + str(p[1]/100) + ')}', end='')

    if p == to_print[-1]:
      print(';')
    else:
      print(' --')

    

  #print('==========================')

       