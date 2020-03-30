import {check, group, sleep} from "k6";
import http from "k6/http";

export let options = {
    thresholds: {
        'http_req_duration{kind:rest}': ["avg<=500"],
    }
};

export default function () {
    group("rest", function () {
        check(http.get("https://test.lokaler.kaufen/api/location/suggestion?zipCode=8", {
            headers: {"Content-Type": "application/json", "Accept": "application/json"},
            tags: {'kind': 'rest'},
        }), {
            "status is 200": (res) => res.status === 200,
        });

        check(http.get("https://test.lokaler.kaufen/api/location/suggestion?zipCode=83", {
            headers: {"Content-Type": "application/json", "Accept": "application/json"},
            tags: {'kind': 'rest'},
        }), {
            "status is 200": (res) => res.status === 200,
        });

        check(http.get("https://test.lokaler.kaufen/api/location/suggestion?zipCode=830", {
            headers: {"Content-Type": "application/json", "Accept": "application/json"},
            tags: {'kind': 'rest'},
        }), {
            "status is 200": (res) => res.status === 200,
        });

        check(http.get("https://test.lokaler.kaufen/api/location/suggestion?zipCode=8302", {
            headers: {"Content-Type": "application/json", "Accept": "application/json"},
            tags: {'kind': 'rest'},
        }), {
            "status is 200": (res) => res.status === 200,
        });

        check(http.get("https://test.lokaler.kaufen/api/location/suggestion?zipCode=83024", {
            headers: {"Content-Type": "application/json", "Accept": "application/json"},
            tags: {'kind': 'rest'},
        }), {
            "status is 200": (res) => res.status === 200,
        });

        check(http.get("https://test.lokaler.kaufen/api/location?zipCode=83024", {
            headers: {"Content-Type": "application/json", "Accept": "application/json"},
            tags: {'kind': 'rest'},
        }), {
            "status is 200": (res) => res.status === 200,
        });

        check(http.get("https://test.lokaler.kaufen/api/shop/nearby?zipCode=83024", {
            headers: {"Content-Type": "application/json", "Accept": "application/json"},
            tags: {'kind': 'rest'},
        }), {
            "status is 200": (res) => res.status === 200,
        });
    });

    group("login", function () {
        check(http.post("https://test.lokaler.kaufen/api/shop/login", JSON.stringify({
            email: "k6@lokaler.kaufen",
            password: "testtest"
        }), {
            headers: {"Content-Type": "application/json", "Accept": "application/json"},
            tags: {'kind': 'rest'},
        }), {
            "status is 401": (res) => res.status === 401,
        });
    });

    sleep(3 * Math.random());
}