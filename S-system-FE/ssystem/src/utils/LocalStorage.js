/*
   AnyX Platform version 2.0

   Copyright ⓒ 2022 anylogic corp. All rights reserved.

   This is a proprietary software of anylogic corp, and you may not use this file except in
   compliance with license agreement with anylogic corp. Any redistribution or use of this
   software, with or without modification shall be strictly prohibited without prior written
   approval of anylogic corp, and the copyright notice above does not evidence any actual or
   intended publication of such software.
*/



// Sets an item with a Key to local storage
const saveStorage = (key, data) => {
  localStorage.setItem(key, JSON.stringify(data));
};

// Looks for a local storage item and returns if present
const getStorage = (key, item) => {
  if( localStorage.getItem(key) && item) {
      const data = JSON.parse(localStorage.getItem(key))
      return data[item]
  }
  else if(localStorage.getItem(key)) {
     return localStorage.getItem(key)
  }
};

// Clear a single item or the whole local storage
const clearStorage = (key='false') => {
  if(key) {
      localStorage.removeItem(key);
  } else {
      localStorage.clear();
  }
}

// Check a single item in local storage
const checkStorage = (key) => {
  const value = localStorage.getItem(key)
  return  value !== undefined
}

// Exports all avaliable functions on the script
export { getStorage, saveStorage, clearStorage, checkStorage }


