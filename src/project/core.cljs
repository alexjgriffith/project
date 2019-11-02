(ns project.core
  (:require [goog.dom :as dom]))


(def params (js-obj "width" 800
                    "height" 600
                    "backgroundColor" "0x1099bb"))
(println params)
;; init
(defonce app (do
               (println "Generating PIXI")
               (let [app (js/PIXI.Application. params)]
                 (-> (dom/getDocument)
                     (.getElementById "app")
                     (dom/append app.view))
                 app)))

(defonce container (let [container (js/PIXI.Container.)]
                     (.addChild app.stage container)
                     container))

(defonce bunny (let [texture (.Texture.from js/PIXI "images/bunny.png")]
                   (loop [x 0]
                     (when (< x 1)
                       (let [bunny (js/PIXI.Sprite. texture)]
                         (set! (.-x bunny) 100)
                         (set! (.-y bunny) 100)                         
                         (.addChild container bunny)
                         bunny)))))

(print (.-x bunny))

(defonce speed {:x 0 :y 0})

(defn up []
  (set! (.-y speed) 10)
  (println "up"))

(defn down []
  (println "down"))

(defn right []
  (println "right"))

(defn left []
  (println "left"))



(defonce bindings (do (.bind js/Mousetrap "w" up)
                      (.bind js/Mousetrap "s" down)
                      (.bind js/Mousetrap "a" right)
                      (.bind js/Mousetrap "d" left)))

;;(println texture)

;; draw update
(defn gameloop [ptime counter]  
  (let [time (.now js/performance)
        dt (- time ptime)
        seconds (.floor js/Math (/ counter 1000))]
    ;;(.rotation container (* dt 0.01))
    (set! (.-y bunny) (- (.-y bunny) (/ (* dt 100) (:y speed)) ))
    (set! (.-x bunny) (- (.-x bunny) (/ (* dt 100) (:x speed)) ))
    ;;(print bunny.x)
    (set! speed {:x 0 :y 0})
    (js/requestAnimationFrame (fn [] (gameloop time (+ dt counter))))))

(defonce gamestart? (gameloop (.now js/performance) 0))

;;(defonce addticker (.add app.ticker (fn [dt] (.rotation container (* dt 0.01)))))
