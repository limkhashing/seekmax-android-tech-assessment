import { Handler } from 'express';
import crypto from 'crypto';
import UserSchema from '../../models/UserSchema';

export const changePasswordHandler: Handler = async (req, res) => {
  const { userId } = req;
  const { password } = req.body;

  try {
    const user = await UserSchema.findOne({ userId });
    if (!user) {
      return res.status(404).json({ message: 'User not found' });
    }

    const salt = crypto.randomBytes(16).toString('hex');
    const crypted = await crypto.scryptSync(password, salt, 64);
    user.password = `${salt}:${crypted.toString('hex')}`;
    await user.save();

    return res.status(201).json({ message: 'Password changed successfully' });
  } catch (err) {
    console.error(err);
    return res.status(500).send();
  }
};
