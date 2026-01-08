const admin = require("firebase-admin");

const serviceAccount = require("./frodobase-key.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://frodoflix-default-rtdb.europe-west1.firebasedatabase.app"
});

const db = admin.database();
const messagesRef = db.ref("/messages");

// Listen for new messages in all existing groups
messagesRef.once("value", snapshot => {
  snapshot.forEach(groupSnapshot => {
    const groupId = groupSnapshot.key;
    const groupMessagesRef = groupSnapshot.ref;

    // Attach child_added listener once per group
    groupMessagesRef.orderByChild("timestamp").on("child_added", async messageSnapshot => {
      const message = messageSnapshot.val();
      if (!message || !message.username) return;

      console.log(`New message in group ${groupId} from ${message.username}: ${message.content}`);

      const payload = {
        notification: {
          title: `New message in group ${groupId}`,
          body: message.content || "New message"
        },
        topic: `group_${groupId}`,
        data: {
          groupId: groupId,
          sender: message.username,
          messageId: messageSnapshot.key
        }
      };

      try {
        const response = await admin.messaging().send(payload);
        console.log(`Notification sent to group_${groupId}`, response);
      } catch (error) {
        console.error("Error sending notification:", error);
      }
    });
  });
});

console.log("Listening for new messages...");
