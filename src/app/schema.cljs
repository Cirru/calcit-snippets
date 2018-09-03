
(ns app.schema )

(def database {:sessions {}, :users {}, :snippets {}})

(def notification {:id nil, :kind nil, :text nil})

(def router {:name nil, :title nil, :data {}, :router nil})

(def session
  {:user-id nil,
   :id nil,
   :nickname nil,
   :router {:name :home, :data nil, :router nil},
   :messages {}})

(def snippet {:id nil, :text "", :frequency 0, :time 0})

(def user {:name nil, :id nil, :nickname nil, :avatar nil, :password nil})
