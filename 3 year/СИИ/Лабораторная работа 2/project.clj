(defproject my-clojure-java-project "0.1.0-SNAPSHOT"
  :description "A project that uses Java and Clojure together"
  :deps {
         io.github.SWI-Prolog/packages-jpl {:git/tag "V9.3.11" :git/sha "dc70a82"}
         }
  :dependencies [
                 [org.clojure/clojure "1.11.1"]
                 [org.projog/projog-core "0.10.0"]]
  :java-source-paths ["src/java"]
  :main ^:skip-aot my-clojure-java-project.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
