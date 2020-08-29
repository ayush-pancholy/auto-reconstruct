# AutoReconstruct
## A Matrix-Based, Machine-Learning Approach to Etymological Reconstruction
### Background
Proto-Indo-European is a hypothesized language that is widely accepted by linguists as the root of all modern Indo-European languages. Thought to have been used between the third and fifth millenia BCE, the language has long been extinct. Consequently, linguists in the modern age have used similarities among words in various Indo-European languages to try to reconstruct the language. One example is of the reconstructed Proto-Indo-European word “phter,” which was reconstructed from similarities in words like the English “father,” the German “vater,” and the Latin “pater.” The goal of this project is to apply a machine learning model to do the job of Proto-Indo-European reconstruction given training data. The machine learning model begins untrained and adjusts itself to minimize error to accurately reconstruct words.
This project was important to me because it combines two of my favorite fields: computer science and linguistics. Additionally, it illustrates how interrelated our world’s ethno-linguistic groups truly are despite their seemingly distinct appearances.
### High-Level Overview
Alphabet Creation. The code first defines an alphabet of phonemes (sounds) such that there is a natural progression. That is, sounds are ordered by their classification as vowels or consonants, then by their origin in the mouth, then followed by the type of sound, and finally by whether they are voiced or unvoiced (whether or not they vibrate the vocal chords, e.g. “zzz” vs “sss”). I created this alphabet after researching phonology in linguistics. It is necessary because using the standard Roman alphabet presents two problems: 
1) Letters, not sounds, are represented. This would imply that phonemes consisting of multiple characters (e.g. “ch,” “sh”) would not be accurately represented.
2) The order bears no phonetic significance. For example, if the program indicated that a word should contain the first letter of the alphabet instead of the second, it would choose ‘a’ over ‘b,’ an entirely different sound despite the fact that the program was only slightly “mistaken.”
### Training Data
To train, the program takes in a set of Indo-European words that have the same root along with the reconstructed Proto-Indo-European word from which they are all derived. The Indo-European languages I chose for this project were English, German, Latin, and Old Norse because they all use Roman script and their relation to Proto-Indo-European is well-documented. A single training datum would contain the reconstructed Proto-Indo-European word along with the derived words in these languages. For example:
	Proto-Indo-European: “manus”
	English: “man”
	Old Norse: “mathr”
	German: “man”
	Latin: “mas”
### Training 
After sufficient data are entered, the program converts each datum into a language-input matrix. Effectively, it takes the English, Old Norse, German, and Latin words and parses them for phonemes in the alphabet. It converts the words to vectors of numerical values based on the positions of the phonemes in the alphabet, and it adds zeros to the vectors until they reach some standard limit n. I’ve set n = 10 phonemes for my program. It then stacks these vectors on top of each other to get the language-input matrix. The order in which they are stacked is irrelevant as long as it is consistent. Weight and bias matrices are then used to perform linear operations on the language-input matrix. The diagonals of the resulting matrix are used as the output word vector, whose numerical values correspond to the phoneme index in the alphabet. The reason why the diagonals are taken is that they represent the influence of phonemes from the same position of related words into account rather than combining phonemes of different slots, which would be illogical. Influence from adjacent phonemes was accounted for using similar matrix operations from secondary weight and bias matrices. The error of the system is found by comparing the output vectors to the Proto-Indo-European word vectors.
### Error Minimization 
The error is regarded as a function of each value in weight and bias matrices, and gradient descent is performed on the error accordingly. The partial derivatives are calculated according to the five-point stencil, a technique for numerical differentiation obtained from Taylor-series expansion. Error minimization completes when either a maximum number of iterations is exceeded or an error threshold is reached. The values for the bias and weight matrices effectively represent the trained model.
### Testing & Results
To prepare training data, cognate words from Old Norse, German, Latin, and English had to be found along with their Proto-Indo-European root reconstructed word. A total of seven sets of these words were used for training and testing. To validate that the model itself was feasible, it was trained on these data and its results were compared to those yielded by a random model. The model consistently reduced the error given by the initial random model by over 90%, indicating that it is valid. Unfortunately, because publicly available data is limited, it was difficult to obtain statistically-significant results when testing the model. However, it should be noted that, when the system was trained on the same seven sets of words, the error compared to random reconstructions of new words seemed to consistently decrease by 10-20%. Additionally, another interesting outcome of the project was that, by looking at the weight and bias matrices after training, I could see the relative degree of relation between Proto-Indo-European and the various languages used for training. For example, the first weight matrix generally had its highest values in the last column, which corresponded with Latin. This is because Latin is closer to Proto-Indo-European historically and linguistically.
