val file = sc.textFile("/Users/lypham/Downloads/spark-3.0.1-bin-hadoop2.7/country_vaccinations/country_vaccinations.csv")
val head = file.first
def isHead(line:String) = line.contains(head)
val remove = file.filter(x => !isHead(x))
val us = remove.filter(x => x.contains("United States"))
import org.apache.spark.ml.linalg.{Vector, Vectors}
def change(row:String) = {
     | val split = row.split(",")
     | val people = split(4).toDouble
     | val date = Vectors.dense(split(2).replace("/","").toDouble)
     | (people,date)
     | }

val all = us.map(x => change(x))
val tf = all.toDF("label","features")
tf.show()
import org.apache.spark.ml.regression.LinearRegression
val lr = new LinearRegression()
val lrModel = lr.fit(tf)
val inter = lrmodel.intercept
val coeff = lrmodel.coefficients
val summ = lrmodel.summary
val mse = summ.meanSquaredError


