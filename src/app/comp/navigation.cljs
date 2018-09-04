
(ns app.comp.navigation
  (:require [hsl.core :refer [hsl]]
            [respo-ui.core :as ui]
            [respo.comp.space :refer [=<]]
            [respo.macros :refer [defcomp <> cursor-> action-> span div]]
            [app.config :as config]
            [respo-alerts.comp.alerts :refer [comp-prompt]]
            [respo-ui.comp.icon :refer [comp-icon]]))

(defcomp
 comp-navigation
 (states logged-in? count-members)
 (div
  {:style (merge
           ui/row-parted
           {:height 48,
            :padding "0 16px",
            :font-size 16,
            :border-bottom (str "1px solid " (hsl 0 0 0 0.1)),
            :font-family ui/font-fancy,
            :background-color (:theme config/site)})}
  (div
   {:on-click (action-> :router/change {:name :home}), :style (merge ui/row-center)}
   (<> (:title config/site) {:cursor :pointer})
   (=< 16 nil)
   (cursor->
    :create
    comp-prompt
    states
    {:multiline? true, :trigger (div {:style {:font-size 20}} (comp-icon :ios-plus-empty))}
    (fn [result d! m!] (d! :snippet/create result))))
  (div
   {:style {:cursor "pointer"}, :on-click (action-> :router/change {:name :profile})}
   (<> (if logged-in? "Me" "Guest"))
   (=< 8 nil)
   (<> count-members))))
