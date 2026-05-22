import Application from './application';
import Router from 'next/router';
import url from 'url';

const redirect = async (urlObject, response) => {
  if (Application.isBrowser) {
    await Router.push(urlObject);
  } else {
    response.writeHead(301, { Location: url.format(urlObject).toString() });
    response.end();
  }
};

export default redirect;
