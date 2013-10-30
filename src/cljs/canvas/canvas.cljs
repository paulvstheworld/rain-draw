(ns canvas.canvas
  (:require [clojure.string :as string]))


(def canvas (.getElementById js/document "my-canvas")) 
(def context (.context canvas "2d"))

(def input-state (atom {})) ;; {:mousedown true/false}

(.addEventListener
 "mousedown"
 (fn [] (swap! input-state (assoc input-state :mousedown true))))

(.addEventListener
 "mouseup"
 (fn [] (swap! input-state (assoc input-state :mousedown false))))

(.addEventListener
 "mousemove"
 (fn [] (swap! input-state (assoc input-state :mousedown true))))


(defn looper [update draw state]
  (.setTimeout
   (fn []
     (let [new-state (update @input-state state)]
       (draw new-state)
       (looper update draw new-state)))
   16))

(defn tick [last-input state]
  (if (:mousedown last-input)
    {:x (+ x 1) :y (+ y 1)}
    state))

(defn draw-rect [state]
  (draw-rect-at (:x state) (:y state)))

(defn draw-rect-at [x y]

  )

(looper tick draw-rect {:x 0 :y 0})
