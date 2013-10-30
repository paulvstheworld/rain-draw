(ns canvas.canvas
  (:require [clojure.string :as string]))

(defn rand-color []
  (str "rgb(" (string/join "," (take 3 (repeatedly #(rand-int 255)))) ")"))

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

(defn looper [update render state]
  (js/setTimeout
   (fn []
     (let [new-state (update @input-state state)]
       (render new-state)
       (looper update render new-state)))
   10))

(defn tick [last-input state]
  (if (:mousedown last-input)
    {:x (:x last-input) :y (:y last-input)}
    state))

(defn draw-rect [state]
  (if (not (empty? state))
      (draw-rect-at (:x state) (:y state))))

(defn draw-rect-at [x y]
  (set! (. context -fillStyle) (rand-color))
  (.fillRect context x y 20 20)
  (.log js/console (str x y)))

(looper tick draw-rect {})
