; rain-draw
;; draw shapes on canvas of various colors
;;      and position depending on their timeout

(ns rain-draw.core
  (:refer-clojure :exclude [map])
  (:require [cljs.core.async :as async
             :refer [<! >! chan put! timeout]]
            [clojure.string :as string])
    (:require-macros [cljs.core.async.macros :refer [go alt!]]))

(def brush-width 30)
(def brush-height 30)

(def c (chan 10))
(def canvas (.getElementById js/document "myCanvas")) 
(def context (.getContext canvas "2d"))



(.addEventListener
 canvas
 "mousedown"
 (fn []
   (go (>! c {:mousedown true}))))


(.addEventListener
 canvas
 "mouseup"
 (fn []
   (go (>! c {:mousedown false}))))

(.addEventListener
 canvas
 "mousemove"
 (fn [e]
   (go (>! c {:x (.-clientX e) :y (.-clientY e)}))))


(defn rand-color []
  (str "rgb(" (string/join "," (take 3 (repeatedly #(rand-int 255)))) ")"))


(defn draw-rect-at [x y]
  (set! (. context -fillStyle) (rand-color))
  (.fillRect context
             (- x (/ brush-width 2))
             (- y (/ brush-height 2))
             brush-width
             brush-height))


(defn looper []
  (go
   (loop [ismousedown false]
     (let [chan-val (<! c)]
       (cond
        (contains? chan-val :mousedown) (recur (chan-val :mousedown))
        (and ismousedown (contains? chan-val :x) (contains? chan-val :y))
        (do
          (draw-rect-at (chan-val :x) (chan-val :y))
          (recur ismousedown))
        :else (recur ismousedown))))))

(looper)
