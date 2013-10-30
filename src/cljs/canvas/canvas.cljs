(ns canvas.canvas
  (:require [clojure.string :as string]))


(def canvas (.getElementById js/document "my-canvas")) 
(def context (.getContext canvas "2d"))

(def input-state (atom {})) ;; {:mousedown true/false}

(.addEventListener
 canvas
 "mousedown"
 (fn [] (swap! input-state assoc :mousedown true)))

(.addEventListener
 canvas
 "mouseup"
 (fn [] (swap! input-state assoc :mousedown false)))

(.addEventListener
 canvas
 "mousemove"
 (fn [e] (swap! input-state assoc :x (.-clientX e) :y (.-clientY e))))

(defmacro unless [test a]
  (if (not test)
     a
     nil))

(defn looper [update render state]
  (js/setTimeout
   (fn []
     (let [new-state (update @input-state state)]
       (render new-state)
       (looper update render new-state)))
   16))

(defn tick [last-input state]
  (if (:mousedown last-input)
    {:x (:x last-input) :y (:y last-input)}
    state))

(defn draw-rect [state]
  (if (not (empty? state))
      (draw-rect-at (:x state) (:y state))))

(defn draw-rect-at [x y]
  (set! (. context -fillStyle) "rgb(12,100,200)")
  (.fillRect context x y 10 10)
  (.log js/console (str x y)))

(looper tick draw-rect {})
