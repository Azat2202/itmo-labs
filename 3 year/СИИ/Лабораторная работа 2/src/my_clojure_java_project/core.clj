(ns my-clojure-java-project.core
  (:gen-class)
  (:import (connector Connector)))

(defmacro try-times
  "Retries expr for times times,
  then throws exception or returns evaluated value of expr"
  [times & expr]
  `(loop [err# (dec ~times)]
     (let [[result# no-retry#] (try [(do ~@expr) true]
                                    (catch Exception e#
                                      (when (zero? err#)
                                        (throw e#))
                                      [nil false]))]
       (if no-retry#
         result#
         (recur (dec err#))))))

(defn ask-user-age []
  (loop []
    (try-times 3 (prn "Сколько вам лет? (введите число)") (Integer/parseInt (read-line)))))

(defn ask-age-rule []
  (let [age (ask-user-age)]
    (cond
      (< age 12) "детская"
      (and (>= age 12) (< age 18)) "подростковая"
      :else "взрослая"
      )))

(def game-types #{:стратегическая
                  :экономическая
                  :карточная
                  :психологическая
                  :интерактивная})

(defn ask-game-type []
  (println "Выберите тип игры который был бы вам более интересен")
  (doseq [type game-types]
    (println (name type)))
  (let [input (keyword (read-line))]
    (if (contains? game-types input)
      (name input)
      (do
        (prn "Такого нет в списке!")
        (recur)))))

(defn ask-player-count []
  (try-times 3 (prn "Какое у вас количество игроков? (введите число)") (Integer/parseInt (read-line))))

(defn ask-player-time []
  (try-times 3 (prn "Какая продолжительность игры (примерная) вас бы устроила?")
             (Integer/parseInt (read-line))))


(defn create-query-statement []
  (let [age-rule (ask-age-rule)
        game-type (ask-game-type)
        player-count (ask-player-count)]
    ;player-time (ask-player-time)]
    (str
      age-rule "(Игра), "
      game-type "(Игра), "
      "минимальное_количество_игроков(Игра, Количество), Количество =< " player-count ". "
      ;"продолжительность(Игра, Время), Время >= " (int (* 0.5 player-time))
      ;", Время =< " (int (* 1.5 player-time)) "."
      )))

;подростковая(Игра),
; стратегическая(Игра),
; минимальное_количество_игроков(Игра, Количество),
; Количество >= 2,
; продолжительность(Игра, Время),
; Время >= 10, Время =< 60. (15, 40)


(defn print-result [result]
  (let [name (.getTerm result "Игра")]
    (prn (str "Игра: " (.toString name)
              )))
  )



(defn recommendation-system []
  (let [result (Connector/executeQuery (create-query-statement))]
    (let [hasAnything (.next result)]
      (cond hasAnything
            (do
              (prn "По вашему запросу нашлись игры: ")
              (print-result result)
              (while (.next result)
                (print-result result)
                ))
            :else (prn "По вашему запросу ничего не нашлось(")))))

(defn -main
  [& args]
  (recommendation-system))
