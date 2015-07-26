(ns colinkahn.flux.dispatcher-test
  (:require [colinkahn.flux.dispatcher :refer [dispatch wait-for set-state!]]
            [cemerick.cljs.test :as t])
  (:require-macros [cemerick.cljs.test
                    :refer [is deftest testing are]]
                   [colinkahn.flux.dispatcher :refer [defhandler]]))

(enable-console-print!)

(deftest test-dispatcher
  (testing "dispatches"
    (def state (set-state! (atom {})))

    (defhandler :comedy [action]
      "was-funny"
      {:funny true :who (:who action)})

    (defhandler :club [action]
      ["was-funny" "was-hilarious"]
      (do
        (wait-for :comedy)
        {:audience :laughs}))

    (dispatch {:type "was-funny" :who "Jerry"})

    (is (= @state {:comedy {:funny true :who "Jerry"} :club {:audience :laughs}}))))

