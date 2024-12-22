import http from "k6/http";
import { check, group, sleep, fail } from "k6";
import { URL } from "https://jslib.k6.io/url/1.0.0/index.js";

export const options = {
  stages: [
    { duration: "100s", target: 1 },
    { duration: "3s", target: 0 },
  ],
};

const BASE_URL = "http://host.docker.internal:8080";

export function setup() {
  var data = {};
  return data;
}

export default function (data) {
  var randomIdx = Math.floor(Math.random() * 6);
  // 랜덤으로 생성, 수정, 삭제
  switch (randomIdx) {
    case (0, 1, 2):
      createItem(__VU);
      break;
    case (3, 4):
      deleteItem(__VU);
      break;
    case 5:
      readItem(__VU);
      break;
  }
}

export function teardown(data) {
  const url = new URL(`${BASE_URL}/sentinel-test`);

  const res = http.get(url.toString(), null);
  console.log("최종 잔여물" + res.body);
}

function createItem(vu) {
  const url = new URL(`${BASE_URL}/sentinel-test`);
  const res = http.post(url.toString(), null, null);

  check(res, {
    "create Successful": (res) => res.status === 200,
  });
}

function readItem(vu) {
  const url = new URL(`${BASE_URL}/sentinel-test`);

  const res = http.get(url.toString(), null); // 응답이 올 때 까지 블로킹됨

  check(res, {
    "find all Successful": (res) => res.status === 200,
  });

  const responseBody = JSON.parse(res.body);
  return responseBody;
}

function deleteItem(vu) {
  const url = new URL(`${BASE_URL}/sentinel-test`);
  const res = http.del(url.toString(), null, null);

  check(res, {
    "del Successful": (res) => res.status === 200,
  });
}
