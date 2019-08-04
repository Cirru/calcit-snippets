
(ns app.comp.workspace
  (:require [respo.core :refer [defcomp <> div input list-> cursor-> button span pre a]]
            [respo.comp.space :refer [=<]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo-ui.core :as ui]
            [app.schema :as schema]
            [app.style :as style]
            [app.config :as config]
            [respo.util.list :refer [map-val]]
            ["copy-text-to-clipboard" :as copy!]
            [respo-alerts.comp.alerts :refer [comp-alert]]
            [cljs.reader :refer [read-string]]
            [clojure.string :as string]
            [respo-alerts.comp.alerts :refer [comp-confirm comp-prompt]]
            [feather.core :refer [comp-i]]
            [app.comp.copied :refer [comp-copied]]
            [hsl.core :refer [hsl]]
            [app.style :as style]
            [calcit-theme.comp.expr :refer [render-expr]]
            [medley.core :refer [filter-vals]]
            [feather.core :refer [comp-icon]])
  (:require-macros [clojure.core.strint :refer [<<]]))

(defcomp
 comp-preview
 (states snippet)
 (let [state (or (:data states) {:show? false})
       title (if (string/blank? (:name snippet)) "Untitled" (:name snippet))]
   (span
    {}
    (span
     {:inner-text title,
      :style {:cursor :pointer},
      :on-click (fn [e d! m!] (m! (update state :show? not)))})
    (if (:show? state)
      (div
       {:style (merge
                ui/column
                {:position :fixed,
                 :width "100%",
                 :height "100%",
                 :left 0,
                 :top 0,
                 :background-color :black,
                 :z-index 100})}
       (div
        {:style (merge
                 ui/row-middle
                 {:padding 8, :border-bottom (str "1px solid " (hsl 0 0 96 0.2))})}
        (<> title)
        (=< 16 nil)
        (comp-icon
         :x
         {:font-size 16, :color (hsl 0 0 100), :cursor :pointer}
         (fn [e d! m!] (m! (assoc state :show? false)))))
       (div
        {:style (merge ui/expand {:padding "16px 8px 200px 8px"})}
        (render-expr (:tree snippet))))))))

(defcomp
 comp-snippet
 (states snippet)
 (div
  {:style (merge
           ui/column
           {:border (<< "1px solid ~(hsl 0 0 30)"),
            :padding 8,
            :margin 8,
            :width 400,
            :display :inline-flex,
            :height 240,
            :vertical-align :top})}
  (div
   {:style ui/row-parted}
   (div
    {:style {:margin-bottom 8, :font-family ui/font-fancy}}
    (cursor-> :preview comp-preview states snippet)
    (=< 8 nil)
    (cursor->
     :name
     comp-prompt
     states
     {:trigger (comp-i :edit-2 16 (hsl 200 80 80)),
      :text "New name:",
      :initial (:name snippet)}
     (fn [result d! m!] (d! :snippet/update-title {:id (:id snippet), :name result}))))
   (div
    {}
    (cursor->
     :remove
     comp-confirm
     states
     {:trigger (a {:style style/link, :inner-text "Del"}), :text "Sure to remove?"}
     (fn [e d! m!] (d! :snippet/remove-one (:id snippet))))
    (=< 16 nil)
    (cursor->
     :edit
     comp-prompt
     states
     {:trigger (a {:style style/link, :inner-text "Update"}),
      :initial (pr-str (:tree snippet)),
      :multiline? true,
      :input-style {:font-family ui/font-code},
      :text "New tree:"}
     (fn [result d! m!]
       (d! :snippet/update-tree {:id (:id snippet), :tree (read-string result)})))))
  (=< 0 16)
  (cursor-> :copy comp-copied states (pr-str (:tree snippet)) (render-expr (:tree snippet)))))

(defcomp
 comp-workspace
 (states snippets)
 (let [state (or (:data states) {:query ""})]
   (div
    {:style {:padding 16, :overflow :auto}}
    (div
     {:style (merge ui/row-parted {:padding "4px 8px"})}
     (span nil)
     (input
      {:style (merge
               ui/input
               {:background-color (hsl 0 0 10 0),
                :color :white,
                :font-family ui/font-normal,
                :border-color (hsl 0 0 100 0.4)}),
       :placeholder "filter..",
       :value (:query state),
       :on-input (fn [e d! m!] (m! (assoc state :query (:value e))))}))
    (list->
     {}
     (->> snippets
          (filter-vals (fn [snippet] (string/includes? (:name snippet) (:query state))))
          (map-val (fn [snippet] (cursor-> (:id snippet) comp-snippet states snippet))))))))
