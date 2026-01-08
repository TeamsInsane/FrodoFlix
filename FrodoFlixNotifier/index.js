const admin = require("firebase-admin");
const serviceAccount = require("./frodobase-key.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://frodoflix-default-rtdb.europe-west1.firebasedatabase.app"
});

const db = admin.database();
const messagesRef = db.ref("/messages");
const groupsRef = db.ref("/groups");

const groupNameCache = new Map();

messagesRef.on("child_added", groupSnapshot => {
  const groupId = groupSnapshot.key;
  const groupMessagesRef = groupSnapshot.ref;

  groupMessagesRef.orderByChild("timestamp").on("child_added", async messageSnapshot => {
    const message = messageSnapshot.val();
    if (!message || !message.username) return;

    try {
      let groupName = groupNameCache.get(groupId);
      if (!groupName) {
        const groupSnap = await groupsRef.child(groupId).once("value");
        groupName = groupSnap.val()?.groupName || groupId;
        groupNameCache.set(groupId, groupName);
      }

      const payload = {
        notification: {
          title: `${groupName}`,
          body: message.username + ": " + message.content
        },
        topic: `group_${groupId}`,
        data: {
          sender: message.username,
        }
      };

      const response = await admin.messaging().send(payload);
      console.log(`Notification sent to group_${groupId}`, response);
    } catch (error) {
      console.error("Error sending notification:", error);
    }
  });
});

console.log("Listening for new messages...");
