(ns
    ^{:doc "Text manipulation utilities not yet in Clojure or Contrib."
      :author "Kyle Burton"}
    clj-etl-utils.text)

(defn uc [#^String s] (.toUpperCase s))
(defn lc [#^String s] (.toLowerCase s))
(defmacro with-tmp-file [[var & [prefix suffix]] & body]
  `(let [prefix# ~prefix
         suffix# ~suffix
         ~var (java.io.File/createTempFile (or prefix# "pfx") (or suffix# "sfx"))]
     ~@body))

(defn md5->string [bytes]
  (let [digester (java.security.MessageDigest/getInstance "MD5")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

(defn sha1->string [bytes]
  (let [digester (java.security.MessageDigest/getInstance "SHA1")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

;; (md5->string (.getBytes "foo bar\n"))
;; (sha1->string (.getBytes "foo bar\n"))

(defn string->sha1 [s]
  (sha1->string (.getBytes s)))

(defn string->md5 [s]
  (md5->string (.getBytes s)))

;; TODO this doesn't belong in text.clj, couldn't think of a better place for it
(defn now-milliseconds []
  (.getTime (java.util.Date.)))


(defn substr [^String s start & [end]]
  (cond
    (and (< start 0)
         (not end))
    (.substring s (+ (count s) start))

    (> start (count s))
    ""
    (or (not end)
        (> end (count s)))
    (.substring s start)

    :else
    (.substring s start end)))



(comment

  (= ""   (substr ""     0     0))
  (= ""   (substr "a"    0     0))
  (= "a"  (substr "a"    0))
  (= "a"  (substr "a"    0     1))
  (= "a"  (substr "a"    0    99))
  (= ""   (substr "a"    99))
  (= ""   (substr "a"    99 199))
  (= "a"  (substr "a"    -1))
  (= "bc" (substr "abc"  -2))

)