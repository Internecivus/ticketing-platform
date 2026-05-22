import React from "react";
import Header from "../components/Header";
import Footer from "../components/Footer";

function DefaultLayout(props) {
  const { children } = props;
  return (
    <React.Fragment>
      <Header />

      <main className="m-3">{children}</main>

      <Footer />
    </React.Fragment>
  );
}

export default DefaultLayout;
