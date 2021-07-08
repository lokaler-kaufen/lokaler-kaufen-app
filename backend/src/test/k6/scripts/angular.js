import {check, group, sleep} from "k6";
import http from "k6/http";

export let options = {
    thresholds: {
        'http_req_duration{kind:html}': ["avg<=500"],
        'http_req_duration{kind:img}': ["avg<=500"],
    }
};

export default function () {
    group("angular", function () {
        check(http.get("https://test.lokaler.kaufen", {
            tags: {'kind': 'html'},
        }), {
            "status is 200": (res) => res.status === 200,
        });
    });

    group("images", function () {
        check(http.get("https://test.lokaler.kaufen/favicon-16x16.png", {
            tags: {'kind': 'img'},
        }), {
            "status is 200": (res) => res.status === 200,
        });

        check(http.get("https://test.lokaler.kaufen/favicon-32x32.png", {
            tags: {'kind': 'img'},
        }), {
            "status is 200": (res) => res.status === 200,
        });
    });

    sleep(5 * Math.random());
}