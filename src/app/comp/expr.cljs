
(ns app.comp.expr
  (:require [respo.macros :refer [defcomp <> div input list-> button span pre]]
            [respo.comp.space :refer [=<]]
            [respo.comp.inspect :refer [comp-inspect]]
            [respo-ui.core :as ui]
            [app.schema :as schema]
            [app.style :as style]
            [app.config :as config]
            [respo.util.list :refer [map-val]]))

(defcomp
 comp-leaf
 (x)
 (span {:inner-text x, :style {:padding "0 4px", :font-family ui/font-code}}))

(defcomp
 comp-expr
 (expr tail?)
 (list->
  {:style (let [simple? (every? string? expr)]
     (merge
      {:border "1px solid #ccc", :border-width "0 0 0 1px", :padding-left 8, :margin-left 8}
      (when (or tail? simple?) {:display :inline-block, :border-width "0 0 1px 0"})
      (when (and tail? (not simple?)) {:border-width "0 0 0 1px"})))}
  (->> expr
       (map-indexed
        (fn [idx x]
          [idx (if (vector? x) (comp-expr x (= (count expr) (inc idx))) (comp-leaf x))])))))
