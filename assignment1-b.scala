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
val x = all.map(x => x._2)
val y = all.map(x => x._1)
val xsum = x.collect.toList
val res = xsum.foldLeft(0.0)(_+_)
val ysum = y.collect.toList
val res2 = ysum.foldLeft(0.0)(_+_) 
val res3 = xsum.zip(ysum).map{case(a,b)=>a*b}.foldLeft(0.0)(_+_)
val res4 = xsum.map(Math.pow(_,2.0)).foldLeft(0.0)(_+_)
val num = (res3 - ((res*res2)/62))
val den = (res4 - ((Math.pow(res,2))/62))
val b1 = num/den
val xmean = res/62
val ymean = res2/62
val b0 = ymean - (b1*xmean)
val res5 = ysum.map(Math.pow(_,2.0)).foldLeft(0.0)(_+_)
val sse = res5 - (b0*res2) - (b1*res3)
val mse = sse/62
println("b0: " + b0)
println("b1: " + b1)