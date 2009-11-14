(ns org.anachronos.clojure.repl-client
  (:import (java.net Socket SocketException)
           (java.io InputStreamReader OutputStream OutputStreamWriter PrintWriter)
           (clojure.lang LineNumberingPushbackReader))
  (:use clojure.main))

(defn- get-console-xml [id] 
	(str 
		"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>"
		(format "<console><info id=\"%s\"/></console>" id)))
  
(defn- info [outs]
	(let 
		[id "123"
		 print (PrintWriter. outs)
		 console-xml (get-console-xml id)]
		 (println (format "%010d%s" (count console-xml) console-xml))
		 (.flush outs)))
		 
(defn- socket-repl [ins outs]
  (binding [*in* (LineNumberingPushbackReader. (InputStreamReader. ins))
            *out* (OutputStreamWriter. outs)
            *err* (PrintWriter. #^OutputStream outs true)]
    (info outs)
    (repl)))
    	
(let 
  [s (Socket. "localhost" 25000)
   ins (.getInputStream s)
   outs (.getOutputStream s)]
	 (socket-repl ins outs))
	 	