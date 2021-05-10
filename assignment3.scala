val rawData = sc.textFile("/Users/lypham/Downloads/spark-3.0.1-bin-hadoop2.7/covtype.data")
import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.regression._
val data = rawData.map { line =>
	val values = line.split(',').map(_.toDouble)
	val featureVector = Vectors.dense(values.init)
	val label = values.last - 1
	LabeledPoint(label, featureVector)
}
val Array(trainData, cvData, testData) = data.randomSplit(Array(0.8, 0.1, 0.1))
trainData.cache()
cvData.cache()
testData.cache()
import org.apache.spark.mllib.evaluation._
import org.apache.spark.mllib.tree._
import org.apache.spark.mllib.tree.model._
import org.apache.spark.rdd._
def getMetrics(model: DecisionTreeModel, data: RDD[LabeledPoint]):
MulticlassMetrics = {
	val predictionsAndLabels = data.map(example =>
		(model.predict(example.features), example.label)
	)
	new MulticlassMetrics(predictionsAndLabels)
}
val model = DecisionTree.trainClassifier(trainData, 7, Map[Int,Int](), "gini", 4, 100)
val metrics = getMetrics(model, cvData)
(0 until 7).map(i => metrics.precision(i)).foreach(println)
metrics.accuracy


