(doseq 
	[url (.getURLs (java.lang.ClassLoader/getSystemClassLoader))] 
	(println (.getFile url)))