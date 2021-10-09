(ns build
  (:refer-clojure :exclude [test])
  (:require [clojure.tools.build.api :as b] ; for b/git-count-revs
            [org.corfield.build :as bb]))

(def lib 'net.clojars.john/injest)
(def version "0.1.0-alpha.22")

;; clojure -T:build ci
;; clojure -T:build deploy

#_ ; alternatively, use MAJOR.MINOR.COMMITS:
(def version (format "1.0.%s" (b/git-count-revs nil)))
(def url "https://github.com/johnmn3/injest")

(def scm {:url url
          :connection "scm:git:git://github.com/johnmn3/injest.git"
          :developerConnection "scm:git:ssh://git@github.com/johnmn3/injest.git"
          :tag version})

(defn test "Run the tests." [opts]
  (bb/run-tests opts))

(defn ci "Run the CI pipeline of tests (and build the JAR)." [opts]
  (-> opts
      (assoc :lib lib :version version :scm scm)
      (bb/run-tests)
      (bb/clean)
      (bb/jar)))

(defn deploy "Deploy the JAR to Clojars." [opts]
  (-> opts
      (assoc :lib lib :version version)
      (bb/deploy)))
