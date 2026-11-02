from django.db import models


class Task(models.Model):
    title = models.CharField(max_length=200)
    done = models.BooleanField(default=False)

    class Meta:
        ordering = ["id"]

    def to_dict(self):
        return {"id": self.id, "title": self.title, "done": self.done}

    def __str__(self):
        return self.title
