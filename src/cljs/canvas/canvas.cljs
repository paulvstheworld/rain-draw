(ns canvas.canvas
  (:require [clojure.string :as string]))

(def flag false)
(def BRUSH-SIZE 10)

(defn rand-color []
  (str "rgb(" (string/join "," (take 3 (repeatedly #(rand-int 255)))) ")"))

(let [canvas (.getElementById js/document
                              "my-canvas")
      context (.getContext canvas "2d")
      mouse-down-handler (fn [] (do
                          (def flag true)))
      mouse-up-handler (fn [] (do
                                (def flag false)))
      mouse-move-handler (fn [e]
                           (if (= flag true)
                             (do
                               (set! (. context -fillStyle) (rand-color))
                               (.fillRect context
                                          (.-clientX e)
                                          (.-clientY e)
                                          BRUSH-SIZE
                                          BRUSH-SIZE))))]

  (.addEventListener canvas "mousedown" (fn [e] (mouse-down-handler)))
  (.addEventListener canvas "mouseup" (fn [e] (mouse-up-handler)))
  (.addEventListener canvas "mousemove" (fn [e] (mouse-move-handler e))))


