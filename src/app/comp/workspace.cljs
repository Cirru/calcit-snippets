
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
            [app.comp.expr :refer [comp-expr]]
            [cljs.reader :refer [read-string]]
            [clojure.string :as string]
            [respo-alerts.comp.alerts :refer [comp-confirm comp-prompt]]
            [respo-ui.comp.icon :refer [comp-icon]]
            [app.comp.copied :refer [comp-copied]]
            [hsl.core :refer [hsl]]
            [app.style :as style])
  (:require-macros [clojure.core.strint :refer [<<]]))

(defcomp
 comp-snippet
 (states snippet)
 (div
  {:style (merge
           ui/column
           {:border (<< "1px solid ~(hsl 0 0 30)"),
            :padding 8,
            :margin 8,
            :width 480,
            :display :inline-flex,
            :height 320,
            :vertical-align :top})}
  (div
   {:style {:margin-bottom 8, :font-family ui/font-fancy}}
   (<> (if (string/blank? (:name snippet)) "Untitled" (:name snippet)))
   (=< 8 nil)
   (cursor->
    :name
    comp-prompt
    states
    {:trigger (comp-icon :edit), :text "New name:", :initial (:name snippet)}
    (fn [result d! m!] (d! :snippet/update-title {:id (:id snippet), :name result}))))
  (=< 0 16)
  (cursor->
   :copy
   comp-copied
   states
   (pr-str (:tree snippet))
   (comp-expr (:tree snippet) false))
  (div
   {:style ui/row-parted}
   (span {})
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
       (d! :snippet/update-tree {:id (:id snippet), :tree (read-string result)})))))))

(defcomp
 comp-workspace
 (states snippets)
 (div
  {:style {:padding 16, :overflow :auto}}
  (list->
   {}
   (->> snippets
        (map-val (fn [snippet] (cursor-> (:id snippet) comp-snippet states snippet)))))))
