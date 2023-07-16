from pysat.formula import CNF
from pysat.solvers import Glucose4

formula = CNF(from_file='clauses/clauses_car_d6_f0_bdd_cw10')

with Glucose4(bootstrap_with=formula.clauses) as m:
  print(m.solve())
  print(m.get_model())
  print(m.get_core())