(defn hello-world
	"The most famous program!"
	{:tag String :complexity 0} ; Some metadata
	([] "Hello")
	([name] (str "Hello" name \!))