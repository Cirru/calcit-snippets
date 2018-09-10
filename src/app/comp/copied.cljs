
(ns app.comp.copied
  (:require [hsl.core :refer [hsl]]
            [app.schema :as schema]
            [respo-ui.core :as ui]
            [respo.macros :refer [defcomp list-> <> span div button]]
            [respo.comp.space :refer [=<]]
            [app.config :as config]
            ["copy-text-to-clipboard" :as copy!]))

(defcomp
 comp-copied
 (states content child)
 (let [state (or (:data states) {:show? false})]
   (div
    {:style (merge ui/flex {:overflow :auto, :cursor :pointer, :position :relative}),
     :on-click (fn [e d! m!]
       (copy! content)
       (m! (assoc state :show? true))
       (js/setTimeout (fn [] (m! (assoc state :show? false))) 1200))}
    child
    (when (:show? state)
      (div
       {:style {:position :absolute,
                :top 0,
                :left 0,
                :background-color :black,
                :color :white,
                :padding "0 8px",
                :font-family ui/font-fancy}}
       (<> "Copied"))))))
