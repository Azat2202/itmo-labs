import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
dataFrame = pd.read_csv('Сиразетдинов Азат Лаба 5 Вариант 27 NORM DATA.csv', sep=';')
dataFrame = dataFrame.drop(['<TICKER>', '<PER>', '<TIME>', '<VOL>'], axis=1)
pd.to_numeric(dataFrame['<OPEN>'])
pd.to_numeric(dataFrame['<HIGH>'])
pd.to_numeric(dataFrame['<LOW>'])
pd.to_numeric(dataFrame['<CLOSE>'])
dataFrame.head()
dataFrame = pd.melt(dataFrame, id_vars=['<DATE>'], value_vars=['<OPEN>', '<HIGH>', '<LOW>', '<CLOSE>'])
print(dataFrame)
sns.boxplot(x='<DATE>',  y='value', data=dataFrame, showfliers=False, color='tomato', hue='variable')
plt.legend()
plt.show()
