from prediction_nb import predict_nb
from prediction_svm import predict_svm
from sentiment import analyse
from speech import recognize
from create_model import text_pre_process

def eval(file_name,choice):
	text = list()
	if choice == 1:
		text.append(recognize(file_name))
	else:
		with open(file_name,"rt") as f:
			lines = f.readlines()
			print(lines)
			for line in lines:
				text.append(line)
	nb_count = 0
	svm_count = 0
	print(text)
	total = len(text)
	for i in text:
		if analyse(i) <= 0.1:
			if predict_nb(i) == 'suicidal':
				nb_count = nb_count+1
			if predict_svm(i) == 'suicidal':
				svm_count = svm_count+1
	avg_count = (nb_count+svm_count)/2
	if choice == 1:
		if avg_count == 1:
			return 'Potentially suicidal content.'
		else:
			return 'No suicidal content found.'
	else:
		print(avg_count)
		print(total)
		percentage = (avg_count/total)*100
		return percentage
