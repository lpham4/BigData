val file = sc.textFile("/Users/lypham/Downloads/block_1.csv")
val head = file.first
def isHead(line:String) = line.contains(head)
val remove = file.filter(x => !isHead(x))
def toDouble(field:String) = {
if ("?".equals(field)) Double.NaN
else field.toDouble
}
def parse(row:String) = {
val split = row.split(",")
val id1 = split(0).toInt
val id2 = split(1).toInt
val is_match = split(11).toBoolean
val result = split.slice(2,11).map(toDouble)
(id1,id2,is_match,result)
}
val all = remove.map(x => parse(x))
val rdd = all.map(x => x._4)
val x = rdd.take(10)
import java.lang.Double.isNaN
val tt = x.map(x => sc.parallelize(x).filter(!isNaN(_)))
val res = tt.map(x => x.stats)

