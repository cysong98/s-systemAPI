/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/

import axios from "axios";
import { makeQueryString } from "../utils/Query.js";
import { env } from "@/api/ApiEnv.js";
const API_BASE_URL = import.meta.env.VITE_APP_API_URL;
// const API_BASE_URL = import.meta.env.production.VITE_APP_API_URL;

const config = {
  baseUrlSbms: API_BASE_URL + "/anyx/common/file/sbms/"
};

const fileAPISbms = {
  fileDownloadApi: conditions => {
    var str = encodeURI(`${makeQueryString(conditions)}`);
      return axios.get(
        `${config.baseUrlSbms}fileDownload/` + str,
        {
          responseType: "arraybuffer"
        }
      );
  },
}

export { fileAPISbms };

