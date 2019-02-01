
(ns app.comp.navigation
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.comp.space :refer [=<]]
            [respo.core
             :refer
             [defcomp <> cursor-> action-> span div input textarea button]]
            [app.config :as config]
            [respo-alerts.comp.alerts :refer [comp-prompt]]
            [respo-ui.comp.icon :refer [comp-icon]]
            [cljs.reader :refer [read-string]]
            [inflow-popup.comp.dialog :refer [comp-dialog]]
            [clojure.string :as string]
            [app.style :as style]))

(defn cirru-expr? [x] (if (vector? x) (every? cirru-expr? x) (string? x)))

(def initial-state {:show? false, :title "", :draft ""})

(def style-container
  (merge
   ui/row-parted
   {:height 48,
    :padding "0 16px",
    :font-size 16,
    :border-bottom (str "1px solid " (hsl 0 0 100 0.3)),
    :font-family ui/font-fancy,
    :background-color (:theme config/site),
    :flex-shrink 0}))

(defcomp
 comp-navigation
 (states logged-in? count-members)
 (let [state (or (:data states) initial-state)]
   (div
    {:style style-container}
    (div
     {:style (merge ui/row-center)}
     (span
      {:on-click (action-> :router/change {:name :home}), :style {:cursor :pointer}}
      (<> (:title config/site)))
     (=< 16 nil)
     (button
      {:style style/button,
       :on-click (fn [e d! m!] (m! (assoc state :show? true))),
       :inner-text "Add"}))
    (div
     {:style {:cursor "pointer"}, :on-click (action-> :router/change {:name :profile})}
     (<> (if logged-in? "Me" "Guest"))
     (=< 8 nil)
     (<> count-members))
    (when (:show? state)
      (comp-dialog
       (fn [mutate!] (mutate! %cursor (assoc state :show? false)))
       (div
        {:style (merge ui/column {:width 640})}
        (input
         {:style ui/input,
          :value (:title state),
          :placeholder "name...",
          :on-input (fn [e d! m!] (m! (assoc state :title (:value e))))})
        (=< nil 16)
        (textarea
         {:style (merge ui/textarea {:font-family ui/font-code, :min-height 240}),
          :placeholder "code...",
          :value (:draft state),
          :on-input (fn [e d! m!] (m! (assoc state :draft (:value e))))})
        (=< nil 16)
        (div
         {:style ui/row-parted}
         (span {})
         (button
          {:style ui/button,
           :inner-text "Create",
           :on-click (fn [e d! m!]
             (let [data (read-string (:draft state))]
               (if (cirru-expr? data)
                 (when (and (not (string/blank? (:title state)))
                            (not (string/blank? (:draft state))))
                   (println "code" (pr-str))
                   (d!
                    :snippet/create
                    {:name (:title state), :tree (read-string (:draft state))})
                   (m! initial-state))
                 (js/alert
                  "Please submit Cirru expressions! For example `[\"def\" \"a\" [\"x\" \"y\"]]`"))))}))))))))
