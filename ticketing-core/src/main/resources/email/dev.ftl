<div style="border:1px solid green; padding: 10px;">
    <h2>Test email</h2>
    <div>
        <div>from: ${data.from}</div>
        <div>to: ${data.to?join(", ")}</div>
        <div>cc: ${data.cc?join(", ")}</div>
        <div>bcc: ${data.bcc?join(", ")}</div>
    </div>
</div>
