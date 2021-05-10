val df = spark.read.options(Map("header" -> "true", "multiline" -> "true", "escape" -> "\"")).csv("/Users/lypham/Downloads/spam_ham_dataset.csv")

val m = df.select("label","text")
val f = m.toDF("label","text")
val test = f.rdd
val some = test.collect()
val s = some.map(row => (row.getString(0), row.getString(1).replaceAll("""[\p{Punct}&&[^.]]""", "").replace("Subject","").replace(".",""))
     | )
val a = sc.makeRDD(s)
val tes = a.toDF("label","text")
tes.show

val split = tes.randomSplit(Array(0.70, 0.30))
val train = split(0)
val test = split(1)
val s = df.filter(col("label").contains("spam"))
val h = df.filter(col("label").contains("ham"))
val ps = s.count
val ph = h.count




